using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class Residente
    {
        public int IdPerfil { get; set; }
        public string Torre { get; set; } = null!;
        public string? Apartamento { get; set; }

        public virtual Perfil IdPerfilNavigation { get; set; } = null!;
    }
}
