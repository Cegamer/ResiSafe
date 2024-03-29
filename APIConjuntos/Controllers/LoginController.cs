using APIConjuntos.DTO;
using APIConjuntos.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace APIConjuntos.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LoginController : ControllerBase
    {
        // POST api/<LoginController>
        [HttpPost]
        public void Post([FromBody] LoginDTO loginInfo)
        {
            appContext dbContext = new appContext();

            if (loginInfo != null)
            {
                var validLogin = dbContext.Usuarios.FirstOrDefault(usuario => usuario.Cedula == loginInfo.Cedula && usuario.Contraseña == loginInfo.Contraseña);

            }
        }

        private readonly string _key = "elpepeetesechSDLGPAPULINCElagrasaNuncaMu3r3";

        /*
        public string GenerateToken(string userId, string userName)
        {
            var tokenHandler = new JwtSecurityTokenHandler();
            var key = Encoding.ASCII.GetBytes(_key);

            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(new[]
                {
                new Claim(ClaimTypes.NameIdentifier, userId),
                new Claim(ClaimTypes.Name, userName),
            }),
                Expires = DateTime.UtcNow.AddDays(7), 
                SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(key), SecurityAlgorithms.HmacSha256Signature)
            };

            var token = tokenHandler.CreateToken(tokenDescriptor);
            return tokenHandler.WriteToken(token);
        }*/
    }
}
