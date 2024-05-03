using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class TiposQuejasReclamo
    {
        public TiposQuejasReclamo()
        {
            QuejasReclamos = new HashSet<QuejasReclamo>();
        }

        public int IdtiposQuejasReclamos { get; set; }
        public string NombreTipo { get; set; } = null!;

        public virtual ICollection<QuejasReclamo> QuejasReclamos { get; set; }
    }
}
