using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class Perfil
    {
        public Perfil()
        {
            PaqueteIdResidenteRecibeNavigations = new HashSet<Paquete>();
            PaqueteIdVigilanteRecibeNavigations = new HashSet<Paquete>();
            RegistroVigilantes = new HashSet<RegistroVigilante>();
            RegistroVisitanteIdResidenteVinculadoNavigations = new HashSet<RegistroVisitante>();
            RegistroVisitanteIdVigilanteQueRegistraNavigations = new HashSet<RegistroVisitante>();
            Reservas = new HashSet<Reserva>();
        }

        public int IdPerfil { get; set; }
        public int IdUsuario { get; set; }
        public int IdConjunto { get; set; }
        public int IdTipoPerfil { get; set; }
        public sbyte Activo { get; set; }

        public virtual Conjunto IdConjuntoNavigation { get; set; } = null!;
        public virtual TiposDePerfil IdTipoPerfilNavigation { get; set; } = null!;
        public virtual Usuario IdUsuarioNavigation { get; set; } = null!;
        public virtual Residente? Residente { get; set; }
        public virtual ICollection<Paquete> PaqueteIdResidenteRecibeNavigations { get; set; }
        public virtual ICollection<Paquete> PaqueteIdVigilanteRecibeNavigations { get; set; }
        public virtual ICollection<RegistroVigilante> RegistroVigilantes { get; set; }
        public virtual ICollection<RegistroVisitante> RegistroVisitanteIdResidenteVinculadoNavigations { get; set; }
        public virtual ICollection<RegistroVisitante> RegistroVisitanteIdVigilanteQueRegistraNavigations { get; set; }
        public virtual ICollection<Reserva> Reservas { get; set; }
    }
}
