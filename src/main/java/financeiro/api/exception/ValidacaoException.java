package financeiro.api.exception;

public class ValidacaoException extends RuntimeException{
    public ValidacaoException(String mensagem){
        super(mensagem);
    }
}
