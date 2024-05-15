using APIConjuntos.DTO;
using APIConjuntos.Models;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Mysqlx;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace APIConjuntos.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ReservasController : ControllerBase
    {

        appContext dbContext = new appContext();
        Mapper mapper = new Mapper(new MapperConfiguration(cfg => cfg.CreateMap<Reserva, ReservasDTO>().ReverseMap()));
        // GET: api/<ReservasController>
        [HttpGet]
        public List<ReservasDTO> Get()
        {
            return mapper.Map<List<Reserva>, List<ReservasDTO>>(dbContext.Reservas.ToList());
        }

        [HttpGet]
        [Route("Zonacomun/{idZonaComun}")]
        public List<ReservasDTO> getReservasByZonaComun([FromRoute] int idZonaComun)
        {
            return mapper.Map<List<Reserva>, List<ReservasDTO>>(dbContext.Reservas.Where(q => q.IdZonaComun == idZonaComun).ToList());
        }



        // GET api/<ReservasController>/5
        [HttpGet("{id}")]
        public ReservasDTO Get(int id)
        {
            return mapper.Map<Reserva, ReservasDTO>(dbContext.Reservas.FirstOrDefault(q => q.IdReserva == id));
        }

        [HttpGet]
        [Route("Zonacomun/{idZonaComun}/{fecha}")]
        public List<ReservasDTO> getReservasByZonaComunFechaEspecifica([FromRoute] int idZonaComun, [FromRoute] string fecha) {
            return mapper.Map<List<Reserva>, List<ReservasDTO>>(dbContext.Reservas.Where(q => q.IdZonaComun == idZonaComun && q.Fecha == DateTime.Parse(fecha)).ToList());
        }


        [HttpGet]
        [Route("Perfil/{idPerfil}")]
        public List<ReservasDTO> getReservasByPerfil([FromRoute] int idPerfil)
        {
            return mapper.Map<List<Reserva>, List<ReservasDTO>>(dbContext.Reservas.Where(q => q.IdReservante == idPerfil).ToList());
        }


        [HttpGet]
        [Route("Conjunto/{idConjunto}")]
        public IActionResult getReservasByConjunto([FromRoute] int idConjunto)
        {
            var query = from reserva in dbContext.Reservas
                        join zonaComun in dbContext.Zonacomuns on reserva.IdZonaComun equals zonaComun.IdZonaComun
                        join perfil in dbContext.Perfils on reserva.IdReservante equals perfil.IdPerfil
                        join usuario in dbContext.Usuarios on perfil.IdUsuario equals usuario.IdUsuario
                        where zonaComun.IdConjunto == idConjunto
                        select new 
                        {
                            IdReserva = reserva.IdReserva,
                            NombreZonaComun = zonaComun.Nombre,
                            NombreReservante = usuario.Nombre,
                            ApellidoReservante = usuario.Nombre,
                            CedulaReservante = usuario.Cedula,
                            FechaReserva = reserva.Fecha,
                            HoraInicio = reserva.HoraInicio,
                            HoraFin = reserva.HoraFin,
                            CantidadPersonas = reserva.CantidadPersonas
                        };

            return new JsonResult(query);
        }


        [HttpGet]
        [Route("Perfil/{idPerfil}/{fecha}")]
        public List<ReservasDTO> getReservasByPerfilYFecha([FromRoute] int idPerfil, [FromRoute] string fecha)
        {
            return mapper.Map<List<Reserva>, List<ReservasDTO>>(dbContext.Reservas.Where(q => q.IdReservante == idPerfil && q.Fecha == DateTime.Parse(fecha)).ToList());
        }

        // POST api/<ReservasController>
        [HttpPost]
        public IActionResult Post([FromBody] ReservasDTO reserva)
        {

            var reservaACrear = mapper.Map<ReservasDTO, Reserva>(reserva);
            if (nuevaReservaValida(reservaACrear))
            {
                dbContext.Reservas.Add(reservaACrear);
                dbContext.SaveChanges();
                return new JsonResult(new { message = "Reserva realizada con exito" });
            }
            return BadRequest("No se pudo hacer la reserva");

        }

        bool nuevaReservaValida(Reserva reserva) { return true; } //Por implementar

        // PUT api/<ReservasController>/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] ReservasDTO reserva)
        {
            dbContext.Reservas.Update(mapper.Map<ReservasDTO, Reserva>(reserva));
            dbContext.SaveChanges();
        }

        // DELETE api/<ReservasController>/5
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            var reservaEliminar = dbContext.Reservas.FirstOrDefault(r => r.IdReserva == id);
            if (reservaEliminar != null)
            {
                dbContext.Reservas.Remove(reservaEliminar);
                dbContext.SaveChanges();
                return new JsonResult(new { message = "Reserva eliminada con exito" });

            }
            return BadRequest("No se pudo eliminar la reserva");
        }
    }
}
