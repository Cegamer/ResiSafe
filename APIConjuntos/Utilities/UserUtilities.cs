using APIConjuntos.Models;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;

namespace APIConjuntos.Utilities
{
    public class UserUtilities
    {
        public static string HashPassword(string password) {
            using(SHA256 sha256 = SHA256.Create())
            {
                byte[] bytes = sha256.ComputeHash(Encoding.UTF8.GetBytes(password));
                byte[] salt = sha256.ComputeHash(Encoding.UTF8.GetBytes("elpepeetesechSDLGPAPULINCElagrasaNuncaMu3r3"));

                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < bytes.Length; i++)
                {
                    sb.Append(bytes[i].ToString("x2"));
                    sb.Append(salt[i].ToString("x2"));
                }


                return sb.ToString();
            }
        
        }


        private static readonly string _key = "calculadora94audifono43svaso12";


        public static string GenerateAuthToken(int userId,int profileId)
        {
            appContext dbcontext = new appContext();

            Usuario userData = dbcontext.Usuarios.First(u => u.IdUsuario == userId);
 
            var tokenHandler = new JwtSecurityTokenHandler();
            var key = Encoding.ASCII.GetBytes(_key);

            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(new[]
                {
                new Claim(ClaimTypes.NameIdentifier, userData.IdUsuario.ToString()),
                new Claim(ClaimTypes.Name, userData.Cedula.ToString()),
                new Claim(ClaimTypes.Role, profileId.ToString())
            }),
                Expires = DateTime.UtcNow.AddDays(7),
                SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(key), SecurityAlgorithms.HmacSha256Signature)
            };

            var token = tokenHandler.CreateToken(tokenDescriptor);
            return tokenHandler.WriteToken(token);
        }
    }
}
