using APIConjuntos.DTO;
using APIConjuntos.Models;
using APIConjuntos.Utilities;
using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Net;
using System.Security.Claims;

namespace APIConjuntos.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ConjuntosController : ControllerBase
    {
        Mapper mapper = new Mapper(new MapperConfiguration(cfg => cfg.CreateMap<Conjunto, ConjuntoDTO>().ReverseMap()));
        appContext dbContext = new appContext();

        // POST: ConjuntosController/Create
        [HttpPost]
        [Authorize]
        [Route("CrearConjunto")]
        public ActionResult Create(ConjuntoDTO conjunto)
        {
            var userProfile = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);

            Perfil perfil = dbContext.Perfils.First(p => p.IdPerfil == userProfile);

            if (dbContext.Conjuntos.Any(c => c.Nombre == conjunto.Nombre))
            {
                var problemDetails = new ProblemDetails
                {
                    Status = (int)HttpStatusCode.BadRequest,
                    Title = "Conjunto Ya existe",
                    Detail = "Conjunto ya existe"
                };
                return Conflict(problemDetails);
            }

            if (conjunto == null)
                return BadRequest(ErrorsUtilities.datosNulos);

            if (perfil.IdTipoPerfil == 4)
            {
                Conjunto conjuntoRegistrar = mapper.Map<ConjuntoDTO, Conjunto>(conjunto);
                dbContext.Conjuntos.Add(conjuntoRegistrar);
                dbContext.SaveChanges();
                return new JsonResult(new { message = "Conjunto Creado" });
            }

            else
                return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);
        }

        [HttpGet]
        public ActionResult getConjuntos()
        {

                List<ConjuntoDTO> conjuntos = mapper.Map<List<Conjunto>, List<ConjuntoDTO>>(dbContext.Conjuntos.ToList());
                return new JsonResult(conjuntos);
        }
        [HttpGet]
        [Authorize]
        [Route("{idConjunto}")]
        public IActionResult getConjunto([FromRoute] int idConjunto)
        {
            var userProfile = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);
            Perfil perfil = dbContext.Perfils.First(p => p.IdPerfil == userProfile);

            if (LogeadoComoAdministradorOAppmaster(idConjunto, perfil))
            {
                ConjuntoDTO conjunto = mapper.Map<ConjuntoDTO>(dbContext.Conjuntos.First(c => c.IdConjunto == idConjunto));
                if (conjunto != null)
                    return new JsonResult(conjunto);
                else return NotFound();
            }
            return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);
        }

        [HttpGet]
        [Authorize]
        [Route("{idConjunto}/Perfiles")]
        public ActionResult getPerfilesConjunto([FromRoute] int idConjunto)
        {
            var userProfile = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);

            Perfil perfil = dbContext.Perfils.First(p => p.IdPerfil == userProfile);

            if (LogeadoComoAdministradorOAppmaster(idConjunto, perfil))
            {
                List<PerfilesDTO> residentes = dbContext.Perfils
                            .Where(p => p.IdTipoPerfil == 2 && p.IdConjunto == idConjunto)
                            .Select(p => mapper.Map<Perfil, PerfilesDTO>(p))
                            .ToList();

                return new JsonResult(residentes);
            }

            else
                return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);
        }


        bool LogeadoComoAdministradorOAppmaster(int idConjunto, Perfil perfil)
        {
            if (perfil.IdTipoPerfil == 4 || (perfil.IdTipoPerfil == 1 && perfil.IdConjunto == idConjunto))
                return true;
            return false;
        }

        [HttpPut]
        [Authorize]
        [Route("{idConjunto}")]
        public IActionResult modificarConjunto([FromRoute] int idConjunto, ConjuntoDTO conjunto) {
            var userProfile = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);
            
            Perfil perfilLogeado = dbContext.Perfils.FirstOrDefault(p => p.IdPerfil == userProfile);

            Conjunto conjuntoAModificar = dbContext.Conjuntos.FirstOrDefault(c => c.IdConjunto == idConjunto);


            if (perfilLogeado == null) return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);
            if (conjuntoAModificar == null) return NotFound();

            if(perfilLogeado.IdTipoPerfil == 4 )
            {
                dbContext.Conjuntos.Update(conjuntoAModificar);
                dbContext.SaveChanges();
                return new JsonResult(new { message = "conjunto modificado" });
            }
            return BadRequest();
        }

        [HttpDelete]
        [Authorize]
        [Route("{idConjunto}")]
        public IActionResult eliminarConjunto([FromRoute] int idConjunto)
        {
            var userProfile = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);

            Perfil perfilLogeado = dbContext.Perfils.FirstOrDefault(p => p.IdPerfil == userProfile);

            Conjunto conjuntoAEliminar = dbContext.Conjuntos.FirstOrDefault(c => c.IdConjunto == idConjunto);


            if (perfilLogeado == null) return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);
            if (conjuntoAEliminar == null) return NotFound();

            if (perfilLogeado.IdTipoPerfil == 4)
            {
                dbContext.Conjuntos.Remove(conjuntoAEliminar);
                dbContext.SaveChanges();
                return new JsonResult(new { message = "conjunto eliminado" });
            }
            return BadRequest();
        }
    }
         
}
