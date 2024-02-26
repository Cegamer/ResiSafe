using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class TiposDePerfil
    {
        public TiposDePerfil()
        {
            Perfils = new HashSet<Perfil>();
        }

        public int IdTipo { get; set; }
        public string NombreTipo { get; set; } = null!;

        public virtual ICollection<Perfil> Perfils { get; set; }
    }
}
