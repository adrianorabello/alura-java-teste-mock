package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @autor adriano rabello 21/02/2021 12:42 PM
 **/
class FinalizarLeilaoServiceTest {


    private FinalizarLeilaoService service;

    @Mock
    private EnviadorDeEmails enviadorDeEmails;

    @Mock
    private LeilaoDao leilaoDao;

    @BeforeEach
    public void before() {

        /** this is for initialize all mocks */
        MockitoAnnotations.initMocks(this);
        this.service = new FinalizarLeilaoService(this.leilaoDao, this.enviadorDeEmails);
    }

    @Test
    void deveriaFinalizarUmLeilao() {

        List<Leilao> leiloes = leiloes();
        Mockito.when(this.leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);
        service.finalizarLeiloesExpirados();
        Leilao leilao = leiloes.get(0);
        Assert.assertTrue(leilao.isFechado());
        Assert.assertEquals(leilao.getLanceVencedor().getValor(), new BigDecimal("900"));

        /** vericy if salvar method is called */
        Mockito.verify(leilaoDao).salvar(leilao);
    }

    @Test
    void deveriaEnviarEmailVencedorDoLeilao() {

        List<Leilao> leiloes = leiloes();
        Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);
        service.finalizarLeiloesExpirados();
        Leilao leilao = leiloes.get(0);
        Lance lanceVencedor = leilao.getLanceVencedor();

        /** verify if enviadorDeEmails sent e-mail for lanceVecedor */
        Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(lanceVencedor);
    }


    @Test
    void naoDevriaEnviarEmailEmCasoDeException() {

        List<Leilao> leiloes = leiloes();
        Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);

        /** thows a exception whem leilaoDao is Called */
        Mockito.when(leilaoDao.salvar(Mockito.any())).thenThrow(RuntimeException.class);

        try {
            service.finalizarLeiloesExpirados();
            /** verify if no interactosn with Mock enviadorDeEmails*/
            Mockito.verifyNoInteractions(enviadorDeEmails);

        } catch (Exception ex) {

        }


    }


    private List<Leilao> leiloes() {

        List<Leilao> lista = new ArrayList<>();

        Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Adriano"));

        Lance primeiro = new Lance(new Usuario("Aline"), new BigDecimal("700"));

        Lance segundo = new Lance(new Usuario("Teste"), new BigDecimal("800"));
        Lance terceiro = new Lance(new Usuario("Clara"), new BigDecimal("900"));

        leilao.propoe(primeiro);
        leilao.propoe(segundo);
        leilao.propoe(terceiro);

        lista.add(leilao);

        return lista;
    }

}