using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class Paquete
    {
        public int IdPaquete { get; set; }
        public string Torre { get; set; } = null!;
        public string? Apto { get; set; }
        public int IdVigilanteRecibe { get; set; }
        public int Estado { get; set; }
        public int IdResidenteRecibe { get; set; }

        public virtual Perfil IdResidenteRecibeNavigation { get; set; } = null!;
        public virtual Perfil IdVigilanteRecibeNavigation { get; set; } = null!;
    }
}
