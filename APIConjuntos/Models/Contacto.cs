using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class Contacto
    {
        public int Idcontacto { get; set; }
        public string Nombre { get; set; } = null!;
        public string Email { get; set; } = null!;
        public string Asunto { get; set; } = null!;
        public string Mensaje { get; set; } = null!;
    }
}
