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

            //if (perfilLogeado == null)
               // return Unauthorized(ErrorsUtilities.sinAccesoAlRecurso);

            var zonaComun = dbContext.Zonacomuns.FirstOrDefault(zc => zc.IdZonaComun == id);

           // if ((perfilLogeado.IdTipoPerfil == 4) || (perfilLogeado.IdConjunto == zonaComun.IdConjunto))
           // {
                return new JsonResult(mapper.Map<Zonacomun,ZonacomunDTO>(zonaComun));
            
           // }
           // return BadRequest();
            
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
                return Ok("Borrado exitoso");
            }
            return BadRequest();
        }

       
        [HttpGet]
        [Route("HorariosDisponibles/{idZonaComun}/{fecha}")]
        public IActionResult ObtenerHorariosDisponibles(int idZonaComun, string fecha)
        {
            // Convertir la fecha de string a DateTime
            DateTime fechaSeleccionada = DateTime.Parse(fecha).Date;

            // Obtener la zona común desde la base de datos
            Zonacomun zonaComun = dbContext.Zonacomuns.FirstOrDefault(zc => zc.IdZonaComun == idZonaComun);

            // Verificar si la zona común existe
            if (zonaComun == null)
            {
                return NotFound("Zona común no encontrada.");
            }

            // Calcular los horarios disponibles para la zona común y la fecha seleccionada
            List<object> horariosConCuposDisponibles = new List<object>();
            DateTime horaInicio = fechaSeleccionada.Add(zonaComun.HorarioApertura);
            DateTime horaFin = fechaSeleccionada.Add(zonaComun.HorarioCierre);

            // Obtener las reservas para la zona común y la fecha seleccionada
            List<Reserva> reservas = dbContext.Reservas
                .Where(r => r.IdZonaComun == idZonaComun && r.Fecha == fechaSeleccionada)
                .ToList();

            while (horaInicio < horaFin)
            {
                DateTime horaFinTemp = horaInicio.AddMinutes(zonaComun.IntervaloTurnos);

                // Calcular el aforo restante para el intervalo de tiempo actual
                int aforoRestante = zonaComun.AforoMaximo - reservas
                    .Where(r => (horaInicio.TimeOfDay >= r.HoraInicio && horaInicio.TimeOfDay < r.HoraFin) || (horaFinTemp.TimeOfDay > r.HoraInicio && horaFinTemp.TimeOfDay <= r.HoraFin))
                    .Sum(r => r.CantidadPersonas);

                horariosConCuposDisponibles.Add(new
                {
                    Fecha = fechaSeleccionada.ToString("MM-dd-yyyy"),
                    HoraInicio = horaInicio.TimeOfDay,
                    HoraFin = horaFinTemp.TimeOfDay,
                    CuposDisponibles = aforoRestante
                });

                horaInicio = horaFinTemp;
            }

            return new JsonResult(horariosConCuposDisponibles);
        }
    }
}
