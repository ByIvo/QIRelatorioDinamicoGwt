package br.com.qileverage.relatoriodinamico.client;

import java.util.List;

import br.com.qileverage.relatoriodinamico.client.relatoriodinamico.QIContainerFiltro;
import br.com.qileverage.relatoriodinamico.entidades.QICampoRelatorio;
import br.com.qileverage.relatoriodinamico.entidades.QIRelatorioDinamico;
import br.com.qileverage.widgets.QIControleMensagens;
import br.com.qileverage.widgets.elementspersonalizados.QIHrWidget;
import br.com.qileverage.widgets.elementspersonalizados.QIPilhaVertical;
import br.com.qileverage.widgets.form.campospersonalizados.QICampo;
import br.com.qileverage.widgets.listbox.QIListBox;
import br.com.qileverage.widgets.mensagens.QIMensagemExibicao.TipoMensagem;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;

public abstract class QITelaRelatorioDinamico extends ScrollPanel
{

	public static String padraoData = "dd/MM/yyyy";
	public static String padraoDataHora = "dd/MM/yyyy";

	private QIPilhaVertical pilhaLayout;

	private QIContainerFiltro filtroCampos;

	private QICampo campoSelecaoRelatorio;
	private QIHrWidget hr;

	private QIListBox<QIRelatorioDinamico> cbRelatorio;

	private Button btnGerar;

	public QITelaRelatorioDinamico(String padraoData, String padraoDataHora)
	{
		QITelaRelatorioDinamico.padraoData = padraoData;
		QITelaRelatorioDinamico.padraoDataHora = padraoDataHora;
		iniciarTela();
	}

	private void iniciarTela()
	{
		instanciarItens();
		setarInformacoes();
		montarTela();
	}

	private void instanciarItens()
	{
		pilhaLayout = new QIPilhaVertical();

		campoSelecaoRelatorio = new QICampo();

		filtroCampos = new QIContainerFiltro();

		btnGerar = new Button("Gerar");

		hr = new QIHrWidget();

		cbRelatorio = new QIListBox<QIRelatorioDinamico>()
		{

			@Override
			public String getTextoExibicao(QIRelatorioDinamico entidade)
			{
				return entidade.getNome();
			}

			@Override
			public String getKey(QIRelatorioDinamico entidade)
			{
				return entidade.hashCode() + "";
			}
		};
	}

	private void setarInformacoes()
	{
		campoSelecaoRelatorio.alterarTextoLabel("Selecionar relatório");
		campoSelecaoRelatorio.alterarCampo(cbRelatorio);

		pilhaLayout.setWidth("100%");
		filtroCampos.setWidth("100%");

		filtroCampos.setHeight(Window.getClientHeight() - 400 + "px");

		btnGerar.setWidth("100px");
		btnGerar.setHeight("30px");
		btnGerar.getElement().getStyle().setPosition(Position.FIXED);
		btnGerar.getElement().getStyle().setBottom(5, Unit.PX);
		btnGerar.getElement().getStyle().setLeft(Window.getClientWidth() / 2 - 50, Unit.PX);

		cbRelatorio.addChangeHandler(new HandlerSelecaoRelatorio());

		btnGerar.addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				setarCamposParaExibir();
				if (!cbRelatorio.getEntidadeSelecionada().getCamposRelatorioQueSeraoExibidos().isEmpty())
				{
					gerarRelatorio();
				}
				else
				{
					QIControleMensagens.mostrarMensagem("Você deve selecionar ao menos uma coluna para gerar o relatório!", TipoMensagem.ALERTA);
				}
			}
		});
	}

	private void setarCamposParaExibir()
	{
		QIRelatorioDinamico relatorio = cbRelatorio.getEntidadeSelecionada();
		List<QICampoRelatorio> camposParaExibirem = filtroCampos.getSelecaoCampos().getAllItensSelecionados();

		relatorio.setListaCampoExibido(camposParaExibirem);
	}

	private void montarTela()
	{

		pilhaLayout.adicionarWidget(campoSelecaoRelatorio);
		pilhaLayout.adicionarWidget(hr);
		pilhaLayout.adicionarWidget(filtroCampos);
		pilhaLayout.adicionarWidget(btnGerar);

		this.setWidget(pilhaLayout);
	}

	public void build(QIRelatorioDinamico... relatorios)
	{
		cbRelatorio.adicionarListaEntidade(relatorios);
	}

	public void build(List<QIRelatorioDinamico> relatorios)
	{
		cbRelatorio.adicionarListaEntidade(relatorios);

		if (!cbRelatorio.isEmpty())
		{
			montarCampos();
		}
	}

	private void montarCampos()
	{
		QIRelatorioDinamico relatorio = cbRelatorio.getEntidadeSelecionada();

		if (relatorio != null)
		{
			filtroCampos.setarNovosCampos(relatorio);
		}
	}

	private class HandlerSelecaoRelatorio implements ChangeHandler
	{

		@Override
		public void onChange(ChangeEvent event)
		{
			montarCampos();
		}

	}

	public QIRelatorioDinamico getRelatorioSelecionado()
	{
		return cbRelatorio.getEntidadeSelecionada();
	}

	public abstract void gerarRelatorio();
}