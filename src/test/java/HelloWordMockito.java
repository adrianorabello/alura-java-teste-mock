import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Leilao;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

/**
 * @autor adriano rabello 21/02/2021 12:27 PM
 **/


public class HelloWordMockito {


    @Test
    void hello() {

        LeilaoDao mock = Mockito.mock(LeilaoDao.class);

        List<Leilao> leiloes = mock.buscarTodos();

        Assert.assertTrue(leiloes.isEmpty());

    }

}
