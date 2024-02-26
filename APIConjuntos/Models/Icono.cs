using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class Icono
    {
        public Icono()
        {
            Zonacomuns = new HashSet<Zonacomun>();
        }

        public int IdIcono { get; set; }
        public string NombreIcono { get; set; } = null!;
        public byte[] Icono1 { get; set; } = null!;

        public virtual ICollection<Zonacomun> Zonacomuns { get; set; }
    }
}
