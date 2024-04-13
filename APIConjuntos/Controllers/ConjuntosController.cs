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

            var conjuntoExiste = dbContext.Conjuntos.First(c => c.Nombre == conjunto.Nombre);
            if(conjuntoExiste != null) {
                var problemDetails = new ProblemDetails
                {
                    Status = (int)HttpStatusCode.BadRequest,
                    Title = "Conjunto Ya existe",
                    Detail = "Conjunto ya existe"
                };
                return Conflict(problemDetails); }

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
        [Authorize]
        public ActionResult getConjuntos()
        {
            var userProfile = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);

            Perfil perfil = dbContext.Perfils.First(p => p.IdPerfil == userProfile);

            if (perfil.IdTipoPerfil == 4)
            {
                List<ConjuntoDTO> conjuntos = mapper.Map<List<Conjunto>, List<ConjuntoDTO>>(dbContext.Conjuntos.ToList());
                return new JsonResult(conjuntos);
            }

            else
                return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);
        }
    }
}
