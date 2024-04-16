using APIConjuntos.DTO;
using APIConjuntos.Models;
using APIConjuntos.Utilities;
using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Net;
using System.Security.Claims;

namespace APIConjuntos.Controllers
{
    [Authorize]
    [Route("api/[controller]")]
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

            if (perfil == null)
                return BadRequest(ErrorsUtilities.datosNulos);
            

            if (perfil.IdUsuario == Convert.ToInt32(userId))
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

            else
            {
                var problemDetails = new ProblemDetails
                {
                    Status = (int)HttpStatusCode.Unauthorized,
                    Title = "No tiene acceso al recurso",
                    Detail = "LOGIN-03",
                };
                return Unauthorized(problemDetails);
            }
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
            else
            {
                var problemDetails = new ProblemDetails
                {
                    Status = (int)HttpStatusCode.Unauthorized,
                    Title = "No tiene acceso al recurso",
                    Detail = "LOGIN-03",
                };
                return Unauthorized(problemDetails);
            }


        }


    }
}
