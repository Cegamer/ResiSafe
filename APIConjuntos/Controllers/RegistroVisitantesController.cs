using APIConjuntos.DTO;
using APIConjuntos.Models;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace APIConjuntos.Controllers
{

    [Route("api/[controller]")]
    [ApiController]
    public class RegistroVisitantesController : ControllerBase
    {


        appContext dbContext = new appContext();
        Mapper mapper = new Mapper(new MapperConfiguration(cfg => cfg.CreateMap<RegistroVisitante, RegistroVisitanteDTO>().ReverseMap()));


        // GET: api/<RegistroVisitantesController>
        [HttpGet]
        public List<RegistroVisitanteDTO> Get()
        {
            return mapper.Map<List<RegistroVisitante>, List<RegistroVisitanteDTO>>(dbContext.RegistroVisitantes.ToList());
        }

        // GET api/<RegistroVisitantesController>/5
        [HttpGet("{id}")]
        public RegistroVisitanteDTO Get(int id)
        {
            return mapper.Map<RegistroVisitante, RegistroVisitanteDTO>(dbContext.RegistroVisitantes.FirstOrDefault(v => v.IdRegistro == id));
        }

        // POST api/<RegistroVisitantesController>
        [HttpPost]
        public void Post([FromBody] RegistroVisitanteDTO registroVisitante)
        {
            dbContext.RegistroVisitantes.Add((mapper.Map<RegistroVisitanteDTO, RegistroVisitante>(registroVisitante)));
            dbContext.SaveChanges();

        }
    }
}
