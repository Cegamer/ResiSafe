using APIConjuntos.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace APIConjuntos.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ContactoController : ControllerBase
    {
        appContext dbContext = new appContext();
        // GET: api/<ContactoController>
        [HttpGet]
        public List<Contacto> Get()
        {
            return dbContext.Contactos.ToList();
        }


        // POST api/<ContactoController>
        [HttpPost]
        public IActionResult Post([FromBody] Contacto datosContacto)
        {
            dbContext.Contactos.Add(datosContacto);
            dbContext.SaveChanges();
            return Ok();
        }


    }
}
