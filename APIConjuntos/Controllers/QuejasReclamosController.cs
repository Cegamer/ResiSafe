using APIConjuntos.DTO;
using APIConjuntos.Models;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace APIConjuntos.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class QuejasReclamosController : ControllerBase
    {


        appContext dbContext = new appContext();
        Mapper mapper = new Mapper(new MapperConfiguration(cfg => cfg.CreateMap<QuejasReclamo, QuejasReclamosDTO>().ReverseMap()));
        // GET: api/<QuejasReclamosController>
        [HttpGet]
        public List<QuejasReclamosDTO> Get()
        {
            return mapper.Map<List<QuejasReclamo>,List<QuejasReclamosDTO>>(dbContext.QuejasReclamos.ToList());
        }

        
        [HttpGet]
        [Route("Cojunto/{idConjunto}")]
        public List<QuejasReclamosDTO> getQuejasByConjunto([FromRoute]  int idConjunto) {
            return mapper.Map<List<QuejasReclamo>, List<QuejasReclamosDTO>>(dbContext.QuejasReclamos.Where(q => q.IdConjunto == idConjunto).ToList());
        }

        // GET api/<QuejasReclamosController>/5
        [HttpGet("{id}")]
        public string Get(int id)
        {
            return "value";
        }

        // POST api/<QuejasReclamosController>
        [HttpPost]
        public void Post([FromBody] QuejasReclamosDTO quejaReclamo)
        {
            dbContext.QuejasReclamos.Add(mapper.Map<QuejasReclamosDTO,QuejasReclamo>(quejaReclamo));
            dbContext.SaveChanges();
        }
    }
}
