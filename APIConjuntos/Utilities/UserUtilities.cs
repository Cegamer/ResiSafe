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
    }
}
