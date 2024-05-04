using APIConjuntos.DTO;
using APIConjuntos.Models;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace APIConjuntos.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TiposQuejasReclamosController : ControllerBase
    {

        appContext dbContext = new appContext();
        Mapper mapper = new Mapper(new MapperConfiguration(cfg => cfg.CreateMap<TiposQuejasReclamosController, TiposQuejasReclamosDTO>().ReverseMap()));
        // GET: api/<TiposQuejasReclamosController>
        [HttpGet]
        public List<TiposQuejasReclamosDTO> Get()
        {
            return mapper.Map<List<TiposQuejasReclamo>, List<TiposQuejasReclamosDTO>>(dbContext.TiposQuejasReclamos.ToList());
        }

        // POST api/<TiposQuejasReclamosController>
        [HttpPost]
        public void Post([FromBody] TiposQuejasReclamosDTO tipoQuejaReclamo)
        {
            dbContext.TiposQuejasReclamos.Add(mapper.Map<TiposQuejasReclamosDTO,TiposQuejasReclamo>(tipoQuejaReclamo));
            dbContext.SaveChanges();
        }
    }
}
