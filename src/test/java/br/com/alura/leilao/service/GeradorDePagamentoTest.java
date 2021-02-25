package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * @autor adriano rabello 21/02/2021 2:20 PM
 **/
class GeradorDePagamentoTest {

    private GeradorDePagamento geradorDePagamento;

    @Mock
    private PagamentoDao pagamentoDao;

    @Captor
    private ArgumentCaptor<Pagamento> captor;

    @BeforeEach
    void beforeEach(){
        MockitoAnnotations.initMocks(this);

        this.geradorDePagamento = new GeradorDePagamento(pagamentoDao);
    }

    @Test
    void deveriaGerarPagemento(){
        Leilao leilao = leilao();
        Lance lanceVencedor = leilao.getLanceVencedor();

        geradorDePagamento.gerarPagamento(lanceVencedor);
        Mockito.verify(pagamentoDao).salvar(captor.capture());
        Pagamento pagamento  = captor.getValue();

        Assert.assertEquals(pagamento.getVencimento(), LocalDate.now().plusDays(1));

        Assert.assertFalse(pagamento.getPago());
        Assert.assertEquals(lanceVencedor.getUsuario(), pagamento.getUsuario());
        Assert.assertEquals(leilao, pagamento.getLeilao());

    }



    private Leilao leilao() {


        Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Adriano"));

        Lance primeiro = new Lance(new Usuario("Aline"), new BigDecimal("600"));

        leilao.propoe(primeiro);

        leilao.setLanceVencedor(primeiro);

        return leilao;

    }
}