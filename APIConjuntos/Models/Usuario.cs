using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class Usuario
    {
        public Usuario()
        {
            Perfils = new HashSet<Perfil>();
        }

        public int IdUsuario { get; set; }
        public string Nombre { get; set; } = null!;
        public string Apellido { get; set; } = null!;
        public int Cedula { get; set; }
        public string Contraseña { get; set; } = null!;
        public byte[] Foto { get; set; } = null!;

        public virtual ICollection<Perfil> Perfils { get; set; }
    }
}
