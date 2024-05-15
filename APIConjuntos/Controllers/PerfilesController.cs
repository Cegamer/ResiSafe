using APIConjuntos.DTO;
using APIConjuntos.Models;
using APIConjuntos.Utilities;
using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Net;
using System.Security.Claims;

namespace APIConjuntos.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PerfilesController : ControllerBase
    {

        appContext dbContext = new appContext();
        Mapper mapper = new Mapper(new MapperConfiguration(cfg => cfg.CreateMap<Perfil, PerfilesDTO>().ReverseMap()));


        // POST: PerfilesController/Create
        [HttpPost]
        [Authorize]
        [Route("CrearPerfil")]
        public IActionResult CrearPerfil(PerfilesDTO perfil)
        {
            var userId = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;
            var perfilId = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);

            Perfil perfilLogeado = dbContext.Perfils.First(p => p.IdPerfil == perfilId);



            if (perfil == null)
                return BadRequest(ErrorsUtilities.datosNulos);


            if ((perfil.IdUsuario == Convert.ToInt32(userId) && perfil.IdTipoPerfil == 2) //Usuario crea su propio perfil de Residente
                || perfilLogeado.IdTipoPerfil == 4 //Appmaster crea el perfil
                || (perfilLogeado.IdConjunto == perfil.IdConjunto && perfilLogeado.IdTipoPerfil == 1)) //Administrador crea Perfil en su conjunto
            {
                Perfil perfilARegistrar = mapper.Map<PerfilesDTO, Perfil>(perfil);
                if (!PerfilExiste(perfilARegistrar))
                {
                    dbContext.Perfils.Add(perfilARegistrar);
                    dbContext.SaveChanges();
                    return new JsonResult(new { message = "Perfil Creado" });
                }
                return new JsonResult(new { message = "Perfil ya existe" });
            }

            return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);
        }

        bool PerfilExiste(Perfil perfil)
        {
            if (dbContext.Perfils.FirstOrDefault(p => p.IdUsuario == perfil.IdUsuario && p.IdConjunto == perfil.IdConjunto && p.IdTipoPerfil == perfil.IdTipoPerfil) != null)
                return true;
            return false;
        }



        [HttpPost]
        [Authorize]
        [Route("IniciarPerfil/{id}")]
        public IActionResult IniciarPerfil([FromRoute] int id)
        {
            var usuarioId = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            var perfil = dbContext.Perfils.Find(id);

            if (perfil == null)
            {
                var problemDetails = new ProblemDetails
                {
                    Status = (int)HttpStatusCode.BadRequest,
                    Title = "El perfil no existe",
                    Detail = "PF-01"
                };

                return BadRequest(problemDetails);
            }

            if (Convert.ToInt32(usuarioId) == perfil.IdUsuario)
            {
                string token = UserUtilities.GenerateAuthToken(Convert.ToInt32(usuarioId), perfil.IdPerfil);
                return new JsonResult(new { Token = token, userID = Convert.ToInt32(usuarioId) });
            }

            else
                return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);



        }

        [HttpGet]
        [Route("{id}")]
        public IActionResult getPerfil([FromRoute] int id) {
            return new JsonResult(mapper.Map<Perfil, PerfilesDTO>( dbContext.Perfils.FirstOrDefault(p => p.IdPerfil == id)));
        }

        [HttpGet]
        [Authorize]
        [Route("DatosPerfil/{idUsuario}")]
        public IActionResult ObtenerPerfiles([FromRoute] int idUsuario)
        {

            var userId = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;
            if (idUsuario == Convert.ToInt32(userId))
            {

                var perfiles = dbContext.Perfils
                                        .Where(p => p.IdUsuario == idUsuario)
                                        .Select(p => new
                                        {
                                            IdPerfil = p.IdPerfil,
                                            NombreConjunto = dbContext.Conjuntos.FirstOrDefault(c => c.IdConjunto == p.IdConjunto).Nombre,
                                            NombreTipoPerfil = dbContext.TiposDePerfils.FirstOrDefault(tp => tp.IdTipo == p.IdTipoPerfil).NombreTipo
                                        })
                                        .ToList();
                return new JsonResult(perfiles);


            }

            return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);

        }

        [HttpGet]
        [Route("BuscarCedula/{cedula}/Conjunto/{idConjunto}")]
        public IActionResult ObtenerPerfilPorCedula([FromRoute] int cedula, [FromRoute] int idConjunto)
        {
            var perfil = (from p in dbContext.Perfils
                          join u in dbContext.Usuarios on p.IdUsuario equals u.IdUsuario
                          where u.Cedula == cedula && p.IdConjunto == idConjunto
                          select p).FirstOrDefault();

            if (perfil == null)
                return NotFound("No se encontró el perfil para la cédula y conjunto especificados.");

            var dataUsuario = dbContext.Usuarios.FirstOrDefault(u => u.IdUsuario == perfil.IdUsuario);
            if (dataUsuario == null)
                return NotFound("No se encontraron los datos del usuario asociados al perfil.");
            
            var result = new
            {
                perfil.IdPerfil,
                perfil.IdConjunto,
                nombreApellido = dataUsuario.Nombre + " " + dataUsuario.Apellido
            };

            return new JsonResult(result);

        }


        [HttpGet]
        [Route("Conjunto/{idConjunto}")]
        [Authorize]
        public IActionResult getPerfilesConjunto([FromRoute] int idConjunto) {

            var perfilId = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);
            Perfil perfilLogeado = dbContext.Perfils.FirstOrDefault(p => p.IdPerfil == perfilId);

            if (perfilLogeado == null)
                return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);

            if ((perfilLogeado.IdTipoPerfil == 4) || (perfilLogeado.IdConjunto == idConjunto && perfilLogeado.IdTipoPerfil == 1)) {


                var perfiles = dbContext.Perfils
                                        .Where(p => p.IdConjunto == idConjunto)
                                        .Select(p => new
                                        {
                                            id = p.IdPerfil,
                                            cedula = dbContext.Usuarios.FirstOrDefault(u => u.IdUsuario == p.IdUsuario).Cedula,
                                            nombre = dbContext.Usuarios.FirstOrDefault(u => u.IdUsuario == p.IdUsuario).Nombre + " " + dbContext.Usuarios.FirstOrDefault(u => u.IdUsuario == p.IdUsuario).Apellido,
                                            tipoPerfil = dbContext.TiposDePerfils.FirstOrDefault(tp => tp.IdTipo == p.IdTipoPerfil).NombreTipo,
                                            Torre = "NIMP",
                                            Apto = 000,
                                            activo = p.Activo
                                        })
                                        .ToList();
                return new JsonResult(perfiles);
            }

            return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);
        }


        [HttpGet]
        [Route("{Cedula}/{idConjunto}")]
        public IActionResult getPerfilResidenteByCedulayConjunto([FromRoute] int Cedula, [FromRoute] int idConjunto) {
            var usuario = dbContext.Usuarios.FirstOrDefault(u => u.Cedula == Cedula);
            
            if (usuario == null) return NotFound("No existe usuario con cedula especificada");

            var perfilResidente = mapper.Map<Perfil, PerfilesDTO>(dbContext.Perfils.FirstOrDefault(p => p.IdUsuario == usuario.IdUsuario && p.IdConjunto == idConjunto && p.IdTipoPerfil == 2));

            if (perfilResidente == null) return NotFound("No existe perfil de residente con la cedula especificada para este conjunto");

            return new JsonResult(perfilResidente);
        }

        [HttpDelete]
        [Authorize]
        [Route("{idPerfiLEliminar}")]
        public IActionResult eliminarPerfil([FromRoute]int idPerfiLEliminar) {

            var perfilId = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);
            Perfil perfilLogeado = dbContext.Perfils.FirstOrDefault(p => p.IdPerfil == perfilId);
            Perfil perfilAEliminar = dbContext.Perfils.FirstOrDefault(p => p.IdPerfil == idPerfiLEliminar);


            if (perfilLogeado == null) return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);
            if (perfilAEliminar == null) return BadRequest(ErrorsUtilities.datosNulos);

            if(perfilLogeado.IdTipoPerfil == 4 || perfilLogeado.IdPerfil == idPerfiLEliminar || (perfilLogeado.IdTipoPerfil == 1 && perfilLogeado.IdConjunto == perfilAEliminar.IdConjunto))
            {
                dbContext.Perfils.Remove(perfilAEliminar);
                dbContext.SaveChanges();
                return new JsonResult(new { message = "Perfil eliminado" });
            }
            return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);
        }

        [HttpPut]
        [Authorize]
        [Route("{idPerfilModificar}")]

        public IActionResult cambiarEstadoPerfil([FromRoute]int idPerfilModificar, int estado) {
            var perfilId = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);
            Perfil perfilLogeado = dbContext.Perfils.FirstOrDefault(p => p.IdPerfil == perfilId);
            Perfil perfilAModificar = dbContext.Perfils.FirstOrDefault(p => p.IdPerfil == idPerfilModificar);


            if (perfilLogeado == null) return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);
            if (perfilAModificar == null) return BadRequest(ErrorsUtilities.datosNulos);

            if (perfilLogeado.IdTipoPerfil == 4 || (perfilLogeado.IdTipoPerfil == 1 && perfilLogeado.IdConjunto == perfilAModificar.IdConjunto))
            {

                if (estado == 1)
                {
                    perfilAModificar.Activo = 1;
                    dbContext.Perfils.Update(perfilAModificar);
                    dbContext.SaveChanges();
                    return new JsonResult(new { message = "Perfil Activado" });
                }
                else if (estado == 0)
                {
                    perfilAModificar.Activo = 0;
                    dbContext.Perfils.Update(perfilAModificar);
                    dbContext.SaveChanges();
                    return new JsonResult(new { message = "Perfil Desactivado" });
                }
                else return BadRequest();
            }
            return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);

        }



    }
}
