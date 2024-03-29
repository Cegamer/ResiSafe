namespace APIConjuntos.Models
{
    public class JwtOptions
    {
        string Issuer;
        string Audience;
        string Key;
        string Subject;

        public JwtOptions(string issuer, string audience, string key, string subject)
        {
            Issuer = issuer;
            Audience = audience;
            Key = key;
            Subject = subject;
        }
    }
}
