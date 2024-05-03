namespace APIConjuntos.DTO
{
    public class VisitanteDTO
    {
        public int IdVisitante { get; set; }
        public string Nombre { get; set; } = null!;
        public string Apellido { get; set; } = null!;
        public int Cedula { get; set; }
        public byte[] Foto { get; set; } = null!;

    }
}
