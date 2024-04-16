using APIConjuntos.DTO;
using APIConjuntos.Models;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace APIConjuntos.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TipoPerfilController : ControllerBase
    {
        appContext dbContext = new appContext();
        Mapper mapper = new Mapper(new MapperConfiguration(cfg => cfg.CreateMap<TiposDePerfil, TipoPerfilDTO>().ReverseMap()));


        // GET: api/<TipoPerfilController>
        [HttpGet]
        public IActionResult Get()
        {
            return  new JsonResult(mapper.Map<List<TiposDePerfil>, List<TipoPerfilDTO>>(dbContext.TiposDePerfils.ToList())) ;
        }
    }
}
