namespace APIConjuntos.DTO
{
    public class ConjuntoDTO
    {
        public int IdConjunto { get; set; }
        public string Nombre { get; set; } = null!;
        public string Direccion { get; set; } = null!;
        public sbyte Activo { get; set; } = 0;

    }
}
