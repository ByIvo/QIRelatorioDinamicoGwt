package br.com.qileverage.relatoriodinamico.client.relatoriodinamico;

import br.com.qileverage.relatoriodinamico.entidades.QICampoRelatorio;
import br.com.qileverage.relatoriodinamico.entidades.QIRelatorioDinamico;
import br.com.qileverage.widgets.QITela;
import br.com.qileverage.widgets.elementspersonalizados.QIPilhaHorizontal;
import br.com.qileverage.widgets.multiselecao.QIMultiSelecao;

import com.google.gwt.user.client.Window;

public class QIContainerFiltro extends QIPilhaHorizontal implements QITela
{
	private QIMultiSelecao<QICampoRelatorio> selecaoCampos;
	private QIFiltroRelatorio filtrosRelatorio;

	public QIContainerFiltro()
	{
		montarItens();
	}

	private void montarItens()
	{
		instanciarItens();
		setarInformacoes();
		montarTela();
	}

	@Override
	public void instanciarItens()
	{
		selecaoCampos = new QIMultiSelecao<QICampoRelatorio>(new QICelulaCampoRelatorio());
		filtrosRelatorio = new QIFiltroRelatorio();
	}

	@Override
	public void setarInformacoes()
	{
		String comprimento = "48%";
		String altura = Window.getClientHeight() - 230 + "px";

		selecaoCampos.setWidth(comprimento);
		selecaoCampos.setHeight(altura);

		filtrosRelatorio.setWidth(comprimento);
		filtrosRelatorio.setHeight(altura);

		selecaoCampos.setNomeCampo("Colunas");
		filtrosRelatorio.setNomeCampo("Filtros");
	}

	@Override
	public void montarTela()
	{
		this.adicionarWidget(selecaoCampos);
		this.adicionarWidget(filtrosRelatorio);
	}

	public QIMultiSelecao<QICampoRelatorio> getSelecaoCampos()
	{
		return selecaoCampos;
	}
	
	public QIFiltroRelatorio getFiltrosRelatorio()
	{
		return filtrosRelatorio;
	}

	public void setarNovosCampos(QIRelatorioDinamico tipoRelatorio)
	{
		selecaoCampos.limparTodos();
		selecaoCampos.setItensNaoSelecionados(tipoRelatorio.getCamposRelatorio());

		filtrosRelatorio.setCamposFiltro(tipoRelatorio);
	}

}
