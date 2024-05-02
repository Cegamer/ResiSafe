using APIConjuntos.DTO;
using APIConjuntos.Models;
using APIConjuntos.Utilities;
using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Security.Claims;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace APIConjuntos.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ZonacomunController : ControllerBase
    {
        Mapper mapper = new Mapper(new MapperConfiguration(cfg => cfg.CreateMap<Zonacomun, ZonacomunDTO>().ReverseMap()));
        appContext dbContext = new appContext();
        // GET: api/<ZonacomunController>
        [HttpGet]
        public IActionResult Get()
        {
            return new JsonResult(mapper.Map<List<Zonacomun>, List<ZonacomunDTO>>(dbContext.Zonacomuns.ToList()));

        }


        [HttpGet]
        [Route("Conjunto/{idConjunto}")]
        [Authorize]
        public IActionResult getZonaComunConjunto([FromRoute] int idConjunto)
        {

            var perfilId = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);
            Perfil perfilLogeado = dbContext.Perfils.FirstOrDefault(p => p.IdPerfil == perfilId);

            if (perfilLogeado == null)
                return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);

            if ((perfilLogeado.IdTipoPerfil == 4) || (perfilLogeado.IdConjunto == idConjunto))
            {
                var zonasComunes = dbContext.Zonacomuns.Where(c => c.IdConjunto == idConjunto).ToList();
                return new JsonResult(mapper.Map<List<Zonacomun>, List<ZonacomunDTO>>(zonasComunes));
            }

            return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);
        }


        // GET api/<ZonacomunController>/5
        [HttpGet("{id}")]
        public IActionResult Get(int id)
        {
            var perfilId = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);
            Perfil perfilLogeado = dbContext.Perfils.FirstOrDefault(p => p.IdPerfil == perfilId);

            if (perfilLogeado == null)
                return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);

            var zonaComun = dbContext.Zonacomuns.FirstOrDefault(zc => zc.IdZonaComun == id);

            if ((perfilLogeado.IdTipoPerfil == 4) || (perfilLogeado.IdConjunto == zonaComun.IdConjunto))
            {
                return new JsonResult(zonaComun);
            
            }
            return BadRequest();
            
        }

        // POST api/<ZonacomunController>
        [HttpPost]
        [Authorize]
        public IActionResult CrearZonaComun(ZonacomunDTO zonaComun)
        {
            var perfilId = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);
            Perfil perfilLogeado = dbContext.Perfils.FirstOrDefault(p => p.IdPerfil == perfilId);

            if (perfilLogeado == null)
                return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);

            bool creacionExitosa = false;
            //Administrador del conjunto crea la zona común
            if (zonaComun.IdConjunto == perfilLogeado.IdConjunto && perfilLogeado.IdTipoPerfil == 1) 
                creacionExitosa = insertarZonaComunEnDB(zonaComun);
            
            //AppMaster crea zona común en un conjunto
            else if (perfilLogeado.IdTipoPerfil == 4) 
                creacionExitosa = insertarZonaComunEnDB(zonaComun);

            else
                return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);

            if(creacionExitosa)
                return new JsonResult(new { message = "Perfil Creado" });

            return BadRequest(ErrorsUtilities.errorAlCrear);

        }

        bool insertarZonaComunEnDB(ZonacomunDTO zonaComun) {
            try
            {
                Zonacomun zonacomunARegistrar = mapper.Map<ZonacomunDTO, Zonacomun>(zonaComun);
                dbContext.Zonacomuns.Add(zonacomunARegistrar);
                dbContext.SaveChanges();
                return true;
            }

            catch (Exception ex) { return false; }
        }

        // DELETE api/<ZonacomunController>/5
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            var perfilId = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);
            Perfil perfilLogeado = dbContext.Perfils.FirstOrDefault(p => p.IdPerfil == perfilId);

            if (perfilLogeado == null)
                return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);

            var zonaComun = dbContext.Zonacomuns.FirstOrDefault(zc => zc.IdZonaComun == id);
            if(zonaComun == null) return NotFound();

            if ((perfilLogeado.IdTipoPerfil == 4) || (perfilLogeado.IdConjunto == zonaComun.IdConjunto))
            {
                dbContext.Zonacomuns.Remove(zonaComun);
                dbContext.SaveChanges();
            }
            return BadRequest();
        }
    }
}
