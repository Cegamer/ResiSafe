using APIConjuntos.DTO;
using APIConjuntos.Models;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace APIConjuntos.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PaquetesController : ControllerBase
    {
        appContext dbContext = new appContext();
        Mapper mapper = new Mapper(new MapperConfiguration(cfg => cfg.CreateMap<Paquete, PaqueteDTO>().ReverseMap()));

        // GET: api/<PaquetesController>
        [HttpGet]
        public List<PaqueteDTO> Get()
        {
            return mapper.Map<List<Paquete>, List<PaqueteDTO>>(dbContext.Paquetes.ToList());
        }

        // GET api/<PaquetesController>/5
        [HttpGet("{id}")]
        public PaqueteDTO Get(int id)
        {
            return mapper.Map<Paquete,PaqueteDTO>(dbContext.Paquetes.FirstOrDefault(p => p.IdPaquete == id));
        }


        [HttpGet]
        [Route("Cojunto/{idConjunto}")]
        public List<PaqueteDTO> getPaquetesByConjunto([FromRoute] int idConjunto)
        {
            return mapper.Map<List<Paquete>, List<PaqueteDTO>>(dbContext.Paquetes.Where(q => q.IdConjunto == idConjunto).ToList());
        }

        // POST api/<PaquetesController>
        [HttpPost]
        public void Post([FromBody] PaqueteDTO paquete)
        {
            dbContext.Paquetes.Add(mapper.Map<PaqueteDTO, Paquete>(paquete));
            dbContext.SaveChanges();
        }

        // PUT api/<PaquetesController>/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] PaqueteDTO paqueteAModificar)
        {
            dbContext.Paquetes.Update(mapper.Map<Paquete>(paqueteAModificar));
            dbContext.SaveChanges();
        }
    }
}
