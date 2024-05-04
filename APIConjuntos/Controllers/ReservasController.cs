using APIConjuntos.DTO;
using APIConjuntos.Models;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;

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
        public List<ReservasDTO> getReservasByZonaComun ([FromRoute] int idZonaComun)
        {
            return mapper.Map<List<Reserva>, List<ReservasDTO>>(dbContext.Reservas.Where(q => q.IdZonaComun == idZonaComun).ToList());
        }



        // GET api/<ReservasController>/5
        [HttpGet("{id}")]
        public ReservasDTO Get(int id)
        {
            return mapper.Map<Reserva,ReservasDTO>(dbContext.Reservas.FirstOrDefault(q => q.IdReserva == id));
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
