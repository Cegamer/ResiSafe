using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class Conjunto
    {
        public Conjunto()
        {
            Paquetes = new HashSet<Paquete>();
            Perfils = new HashSet<Perfil>();
            QuejasReclamos = new HashSet<QuejasReclamo>();
            Zonacomuns = new HashSet<Zonacomun>();
        }

        public int IdConjunto { get; set; }
        public string Nombre { get; set; } = null!;
        public string Direccion { get; set; } = null!;
        public sbyte Activo { get; set; }

        public virtual ICollection<Paquete> Paquetes { get; set; }
        public virtual ICollection<Perfil> Perfils { get; set; }
        public virtual ICollection<QuejasReclamo> QuejasReclamos { get; set; }
        public virtual ICollection<Zonacomun> Zonacomuns { get; set; }
    }
}
