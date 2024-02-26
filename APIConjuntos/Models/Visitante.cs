using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class Visitante
    {
        public Visitante()
        {
            RegistroVisitantes = new HashSet<RegistroVisitante>();
        }

        public int IdVisitante { get; set; }
        public string Nombre { get; set; } = null!;
        public string Apellido { get; set; } = null!;
        public int Cedula { get; set; }
        public byte[] Foto { get; set; } = null!;

        public virtual ICollection<RegistroVisitante> RegistroVisitantes { get; set; }
    }
}
