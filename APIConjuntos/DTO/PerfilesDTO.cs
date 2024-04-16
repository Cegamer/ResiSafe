namespace APIConjuntos.DTO
{
    public class PerfilesDTO
    {
        public int IdPerfil { get; set; }
        public int IdUsuario { get; set; }
        public int IdConjunto { get; set; }
        public int IdTipoPerfil { get; set; }
        public sbyte Activo { get; set; } = 0;
    }
}
