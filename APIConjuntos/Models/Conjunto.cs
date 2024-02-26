using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class Conjunto
    {
        public Conjunto()
        {
            Perfils = new HashSet<Perfil>();
            Zonacomuns = new HashSet<Zonacomun>();
        }

        public int IdConjunto { get; set; }
        public string Nombre { get; set; } = null!;
        public string Direccion { get; set; } = null!;

        public virtual ICollection<Perfil> Perfils { get; set; }
        public virtual ICollection<Zonacomun> Zonacomuns { get; set; }
    }
}
