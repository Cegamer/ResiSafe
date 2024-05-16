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
        public IActionResult getReservasByZonaComun([FromRoute] int idZonaComun)
        {
            var query = from reserva in dbContext.Reservas
                        join zonaComun in dbContext.Zonacomuns on reserva.IdZonaComun equals zonaComun.IdZonaComun
                        join perfil in dbContext.Perfils on reserva.IdReservante equals perfil.IdPerfil
                        join usuario in dbContext.Usuarios on perfil.IdUsuario equals usuario.IdUsuario
                        where zonaComun.IdZonaComun == idZonaComun
                        select new
                        {
                            IdReserva = reserva.IdReserva,
                            NombreZonaComun = zonaComun.Nombre,
                            NombreReservante = usuario.Nombre,
                            ApellidoReservante = usuario.Nombre,
                            CedulaReservante = usuario.Cedula,
                            FechaReserva = reserva.Fecha.Date.ToString("yyyy-MM-dd"),
                            HoraInicio = reserva.HoraInicio,
                            HoraFin = reserva.HoraFin,
                            CantidadPersonas = reserva.CantidadPersonas,
                            Estado = reserva.Estado
                        };

            return new JsonResult(query);
        }



        // GET api/<ReservasController>/5
        [HttpGet("{id}")]
        public ReservasDTO Get(int id)
        {
            return mapper.Map<Reserva, ReservasDTO>(dbContext.Reservas.FirstOrDefault(q => q.IdReserva == id));
        }


        [HttpGet]
        [Route("Perfil/{idPerfil}")]
        public IActionResult getReservasByPerfil([FromRoute] int idPerfil)
        {
            var query = from reserva in dbContext.Reservas
                        join zonaComun in dbContext.Zonacomuns on reserva.IdZonaComun equals zonaComun.IdZonaComun
                        join perfil in dbContext.Perfils on reserva.IdReservante equals perfil.IdPerfil
                        join usuario in dbContext.Usuarios on perfil.IdUsuario equals usuario.IdUsuario
                        where reserva.IdReservante == idPerfil
                        select new
                        {
                            IdReserva = reserva.IdReserva,
                            NombreZonaComun = zonaComun.Nombre,
                            NombreReservante = usuario.Nombre,
                            ApellidoReservante = usuario.Nombre,
                            CedulaReservante = usuario.Cedula,
                            FechaReserva = reserva.Fecha.Date.ToString("yyyy-MM-dd"),
                            HoraInicio = reserva.HoraInicio,
                            HoraFin = reserva.HoraFin,
                            CantidadPersonas = reserva.CantidadPersonas,
                            Estado = reserva.Estado,

                        };

            return new JsonResult(query);
        }


        // POST api/<ReservasController>
        [HttpPost]
        public IActionResult Post([FromBody] ReservasDTO reserva)
        {

            //Implementar logica de estado de reserva dependiendo del precio

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
        public IActionResult Put(int id, [FromBody] int estado)
        {
            var reserva = dbContext.Reservas.FirstOrDefault(r => r.IdReserva == id);
            if (reserva == null) return NotFound();
            reserva.Estado = estado;
            dbContext.Reservas.Update(reserva);
            dbContext.SaveChanges();
            return new JsonResult(new { message = "estado de reserva cambiado con exito" });
        }
    }
}
