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


        [HttpGet]
        [Route("Conjunto/{idConjunto}")]
        public IActionResult getVisitasbyConjunto([FromRoute] int idConjunto) {
            var visitas = (from registro in dbContext.RegistroVisitantes
                           join visitante in dbContext.Visitantes on registro.IdVisitante equals visitante.IdVisitante
                           join perfil in dbContext.Perfils on registro.IdResidenteVinculado equals perfil.IdPerfil
                           join usuario in dbContext.Usuarios on perfil.IdUsuario equals usuario.IdUsuario
                           where perfil.IdConjunto == idConjunto
                           select new
                           {
                               registro.IdRegistro,
                               visitante.Nombre,
                               visitante.Apellido,
                               visitante.Cedula,
                               fecha = registro.Fecha.Date.ToString("yyyy-MM-dd"),
                               registro.HoraIngreso,
                               NombreResidente = usuario.Nombre,
                               ApellidoResidente = usuario.Apellido,
                               CedulaResidente = usuario.Cedula
                           }).ToList();
            return new JsonResult( visitas);
        }


        [HttpGet]
        [Route("Residente/{idResidente}")]
        public IActionResult getVisitasbyResidente([FromRoute] int idResidente)
        {
            var visitas = (from registro in dbContext.RegistroVisitantes
                           join visitante in dbContext.Visitantes on registro.IdVisitante equals visitante.IdVisitante
                           join perfil in dbContext.Perfils on registro.IdResidenteVinculado equals perfil.IdPerfil
                           join usuario in dbContext.Usuarios on perfil.IdUsuario equals usuario.IdUsuario
                           where perfil.IdPerfil == idResidente
                           select new
                           {
                               registro.IdRegistro,
                               visitante.Nombre,
                               visitante.Apellido,
                               visitante.Cedula,
                               fecha = registro.Fecha.Date.ToString("yyyy-MM-dd"),
                               registro.HoraIngreso,
                               NombreResidente = usuario.Nombre,
                               ApellidoResidente = usuario.Apellido,
                               CedulaResidente = usuario.Cedula
                           }).ToList();
            return new JsonResult(visitas);
            return new JsonResult(visitas);
        }

        // POST api/<RegistroVisitantesController>
        [HttpPost]
        public IActionResult Post([FromBody] RegistroVisitanteDTO registroVisitante)
        {
            dbContext.RegistroVisitantes.Add((mapper.Map<RegistroVisitanteDTO, RegistroVisitante>(registroVisitante)));
            dbContext.SaveChanges();
            return new JsonResult(new { message = "Registro Exitoso"} );

        }


    }
}
