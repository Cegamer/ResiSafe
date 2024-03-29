using APIConjuntos.DTO;
using APIConjuntos.Models;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Diagnostics;
using System.Net;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace APIConjuntos.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {

        appContext dbContext = new appContext();
        Mapper mapper = new Mapper(new MapperConfiguration(cfg => cfg.CreateMap<Usuario, UsuariosDTO>().ReverseMap()));

        // GET: api/<UsersController>
        [HttpGet]
        public List<UsuariosDTO> Get()
        {
            return mapper.Map<List<Usuario>, List<UsuariosDTO>>(dbContext.Usuarios.ToList()); ;
        }

        // GET api/<UsersController>/5
        [HttpGet("{id}")]
        public UsuariosDTO Get(int id)
        {
            return mapper.Map<UsuariosDTO>(dbContext.Usuarios.First(u => u.IdUsuario == id));
        }

        [HttpPost]
        public IActionResult Post(RegisterDTO registerInfo)
        {

            Mapper mapperRegister = new Mapper(new MapperConfiguration(cfg => cfg.CreateMap<Usuario, RegisterDTO>().ReverseMap()));

            if (registerInfo == null)
            {
                var problemDetails = new ProblemDetails
                {
                    Status = (int)HttpStatusCode.BadRequest,
                    Title = "Los datos de registro no pueden ser nulos",
                    Detail = "No se ha ingresado ningún dato"
                };

                return BadRequest(problemDetails);
            }

            try
            {
                appContext dbContext = new appContext();
                Usuario usuarioARegistar = mapperRegister.Map<RegisterDTO, Usuario>(registerInfo);
                dbContext.Add(usuarioARegistar);
                dbContext.SaveChanges();
                return Ok("Registro exitoso");
            }

            catch (Exception ex)
            {
                var problemDetails = new ProblemDetails
                {
                    Status = (int)HttpStatusCode.InternalServerError,
                    Title = "Error al registrar el usuario",
                    Detail = ex.ToString()
                };
                return StatusCode(problemDetails.Status.Value, problemDetails);
            }
           
        } 


        // PUT api/<UsersController>/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] UsuariosDTO usuario)
        {
            dbContext.Usuarios.Update(mapper.Map<Usuario>(usuario));
            dbContext.SaveChanges();
        }

        // DELETE api/<UsersController>/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
            dbContext.Usuarios.Remove(dbContext.Usuarios.First(u => u.IdUsuario == id));
            dbContext.SaveChanges();
        }
    }
}
