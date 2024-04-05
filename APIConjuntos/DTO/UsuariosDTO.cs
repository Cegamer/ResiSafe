using System.Text;

namespace APIConjuntos.DTO
{
    public class UsuariosDTO
    {
        public int IdUsuario { get; set; }
        public string Nombre { get; set; } = null!;
        public string Apellido { get; set; } = null!;
        public int Cedula { get; set; }
        public string Contraseña { get; set; } = null!;
        public byte[] Foto { get; set; } = null!;

        public UsuariosDTO(int idUsuario, string nombre, string apellido, int cedula, string contraseña, byte[] foto)
        {
            IdUsuario = idUsuario;
            Nombre = nombre;
            Apellido = apellido;
            Cedula = cedula;
            Contraseña = contraseña;
            Foto = foto;
        }
    }

    public class LoginDTO {
        public int Cedula { get; set; }
        public string Contraseña { get; set; } = null!;

        public LoginDTO(int cedula, string contraseña)
        {
            Cedula = cedula;
            Contraseña = contraseña;
        }
    }

    public class RegisterDTO {

        public string Nombre { get; set; } = null!;
        public string Apellido { get; set; } = null!;
        public int Cedula { get; set; }
        public string Contraseña { get; set; } = null!;

        public RegisterDTO(string nombre, string apellido, int cedula, string contraseña)
        {
            Nombre = nombre;
            Apellido = apellido;
            Cedula = cedula;
            Contraseña = contraseña;
        }

    }
}
