using System.Text;

namespace APIConjuntos.DTO
{
    public class UsuariosDTO
    {
        public UsuariosDTO(int idUsuario, string nombre, string apellido, int cedula, string contraseña, byte[] foto)
        {
            IdUsuario = idUsuario;
            Nombre = nombre;
            Apellido = apellido;
            Cedula = cedula;
            Contraseña = contraseña;
            Foto = foto;
        }

        public int IdUsuario { get; set; }
        public string Nombre { get; set; } = null!;
        public string Apellido { get; set; } = null!;
        public int Cedula { get; set; }
        public string Contraseña { get; set; } = null!;
        public byte[] Foto { get; set; } = null!;
    }

    public class LoginDTO {
        public LoginDTO(int cedula, string contraseña)
        {
            Cedula = cedula;
            Contraseña = contraseña;
        }

        public int Cedula { get; set; }
        public string Contraseña { get; set; } = null!;
    }

    public class RegisterDTO {
        public RegisterDTO(string nombre, string apellido, int cedula, string contraseña, byte[] foto)
        {
            Nombre = nombre;
            Apellido = apellido;
            Cedula = cedula;
            Contraseña = contraseña;
        }

        public string Nombre { get; set; } = null!;
        public string Apellido { get; set; } = null!;
        public int Cedula { get; set; }
        public string Contraseña { get; set; } = null!;

    }
}
