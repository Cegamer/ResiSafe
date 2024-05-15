namespace APIConjuntos.DTO
{
    public class QuejasReclamosDTO
    {
        public int IdquejasReclamos { get; set; }
        public int IdTipo { get; set; }
        public string QuejaReclamo { get; set; } = null!;
        public int IdConjunto { get; set; }
        public int IdPersonaQueEnvia { get; set; }

    }
}
