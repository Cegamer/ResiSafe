using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class QuejasReclamo
    {
        public int IdquejasReclamos { get; set; }
        public int IdTipo { get; set; }
        public string QuejaReclamo { get; set; } = null!;
        public int IdConjunto { get; set; }
        public int IdPersonaQueEnvia { get; set; }

        public virtual Conjunto IdConjuntoNavigation { get; set; } = null!;
        public virtual Perfil IdPersonaQueEnviaNavigation { get; set; } = null!;
        public virtual TiposQuejasReclamo IdTipoNavigation { get; set; } = null!;
    }
}
