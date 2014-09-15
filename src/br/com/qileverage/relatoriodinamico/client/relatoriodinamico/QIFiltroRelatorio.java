package br.com.qileverage.relatoriodinamico.client.relatoriodinamico;

import br.com.qileverage.relatoriodinamico.client.QITelaRelatorioDinamico;
import br.com.qileverage.relatoriodinamico.entidades.QICampoRelatorio;
import br.com.qileverage.relatoriodinamico.entidades.QIFiltroCampoRelatorio;
import br.com.qileverage.relatoriodinamico.entidades.QIRelatorioDinamico;
import br.com.qileverage.relatoriodinamico.entidades.TFiltro;
import br.com.qileverage.widgets.QIControleMensagens;
import br.com.qileverage.widgets.QITela;
import br.com.qileverage.widgets.elementspersonalizados.QIPilhaHorizontal;
import br.com.qileverage.widgets.elementspersonalizados.QIPilhaVertical;
import br.com.qileverage.widgets.funcoes.CControleFuncoesClient;
import br.com.qileverage.widgets.listbox.QIListBox;
import br.com.qileverage.widgets.resources.Resources;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class QIFiltroRelatorio extends QIPilhaVertical implements QITela
{
	private QIRelatorioDinamico tipoRelatorio;

	private Label lblNomeCampo;

	private QIPilhaHorizontal pilhaCampoFiltros;
	private QIPilhaHorizontal pilhaLabelCampoFiltros;

	private QIListBox<QICampoRelatorio> listaCamposRelatorio;
	private QIListBox<TFiltro> listaFiltroExistente;
	private TextBox txbValorFiltro;

	private PushButton btnAdicionarNovoFiltro;

	private QIGridFiltrosAdicionados gridFiltrosAdicionados;

	public QIFiltroRelatorio()
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
		lblNomeCampo = new Label();

		pilhaCampoFiltros = new QIPilhaHorizontal();
		pilhaLabelCampoFiltros = new QIPilhaHorizontal();

		txbValorFiltro = new TextBox();

		btnAdicionarNovoFiltro = new PushButton(new Image(Resources.INSTANCE.imgAdd()));

		gridFiltrosAdicionados = new QIGridFiltrosAdicionados()
		{

			@Override
			public void excluirEntidade(QIFiltroCampoRelatorio entidade)
			{
				tipoRelatorio.removerFiltro(entidade);
				this.removerEntidade(entidade);
			}
		};

		listaCamposRelatorio = new QIListBox<QICampoRelatorio>()
		{

			@Override
			public String getTextoExibicao(QICampoRelatorio entidade)
			{
				return entidade.getNomeCampo();
			}

			@Override
			public String getKey(QICampoRelatorio entidade)
			{
				return entidade.getNomeCampo();
			}
		};

		listaFiltroExistente = new QIListBox<TFiltro>()
		{

			@Override
			public String getTextoExibicao(TFiltro entidade)
			{
				return entidade.toString();
			}

			@Override
			public String getKey(TFiltro entidade)
			{
				return entidade.name();
			}
		};

	}

	@Override
	public void setarInformacoes()
	{
		listaCamposRelatorio.addChangeHandler(new ChangeHandlerCampoRelatorio());

		btnAdicionarNovoFiltro.addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				adicionarNovoFiltro();
			}
		});

		gridFiltrosAdicionados.setWidth("95%");
	}

	@Override
	public void montarTela()
	{
		String tamanhoLabelCampos = "33%";
		adicionarLabelCampoFiltros("Coluna", tamanhoLabelCampos);
		adicionarLabelCampoFiltros("Tipo de Filtro", tamanhoLabelCampos);
		adicionarLabelCampoFiltros("Valor do Filtro", tamanhoLabelCampos);

		String tamanhoCampos = "31%";
		adicionarCampoFiltro(listaCamposRelatorio, tamanhoCampos);
		adicionarCampoFiltro(listaFiltroExistente, tamanhoCampos);
		adicionarCampoFiltro(txbValorFiltro, "25%");
		adicionarCampoFiltro(btnAdicionarNovoFiltro, "");

		this.adicionarWidget(lblNomeCampo);
		this.adicionarWidget(pilhaLabelCampoFiltros);
		this.adicionarWidget(pilhaCampoFiltros);
		this.adicionarWidget(gridFiltrosAdicionados);
	}

	private void adicionarLabelCampoFiltros(String nome, String tamanho)
	{
		Label lblNome = new Label(CControleFuncoesClient.getNomeCampoComSeparador(nome));

		lblNome.getElement().getStyle().setProperty("width", tamanho);

		pilhaLabelCampoFiltros.adicionarWidget(lblNome);
	}

	private void adicionarCampoFiltro(Widget campo, String tamanho)
	{
		if (!tamanho.isEmpty())
		{
			campo.getElement().getStyle().setProperty("width", tamanho);
		}
		campo.getElement().getStyle().setMargin(5, Unit.PX);
		campo.getElement().getStyle().setProperty("boxSizing", "border-box");
		pilhaCampoFiltros.adicionarWidget(campo);
	}

	public void setNomeCampo(String nomeCampo)
	{
		lblNomeCampo.setText(nomeCampo);
	}

	public void setCamposFiltro(QIRelatorioDinamico tipoRelatorio)
	{
		this.tipoRelatorio = tipoRelatorio;
		listaCamposRelatorio.clear();
		listaCamposRelatorio.adicionarListaEntidade(tipoRelatorio.getCamposRelatorio());

		atualizarFiltrosExistentes();
	}

	private void atualizarFiltrosExistentes()
	{
		gridFiltrosAdicionados.limparListaEntidades();
		gridFiltrosAdicionados.adicionarListaEntidades(tipoRelatorio.getFiltrosCamposRelatorio());

		QICampoRelatorio campoRelatorioSelecionado = listaCamposRelatorio.getEntidadeSelecionada();
		if (campoRelatorioSelecionado != null)
		{
			listaFiltroExistente.limpar();
			listaFiltroExistente.adicionarListaEntidade(campoRelatorioSelecionado.getTipoCampo().getFiltros());
		}
	}

	public void adicionarNovoFiltro()
	{
		Object objetoDigitado = validarFiltro();
		if (objetoDigitado != null)
		{
			TFiltro tipoFiltro = listaFiltroExistente.getEntidadeSelecionada();
			if (tipoFiltro != null)
			{
				QIFiltroCampoRelatorio filtro = new QIFiltroCampoRelatorio();
				filtro.setTipoFiltro(tipoFiltro);
				filtro.setValue(objetoDigitado);
				filtro.setCampoRelatorio(listaCamposRelatorio.getEntidadeSelecionada());

				this.tipoRelatorio.adicionarFiltro(filtro);
				gridFiltrosAdicionados.adicionarEntidade(filtro);

				txbValorFiltro.setText("");
			}
		}
	}

	private Object validarFiltro()
	{
		if (this.listaCamposRelatorio.getEntidadeSelecionada() == null)
		{
			QIControleMensagens.mostrarMensagemInstantanea("Você deve selecionar um campo para inserir o filtro!");
			return null;
		}

		Object retorno = validarConteudo(txbValorFiltro.getText());

		if (retorno == null)
		{
			QIControleMensagens.mostrarMensagemInstantanea("O valor informado não corresponde com o tipo de filtro!");
		}

		return retorno;
	}

	private Object validarConteudo(String conteudo)
	{
		QICampoRelatorio campoRelatorioSelecionado = listaCamposRelatorio.getEntidadeSelecionada();

		if (campoRelatorioSelecionado != null)
		{
			switch (campoRelatorioSelecionado.getTipoCampo())
			{
				case NUMERO:
					try
					{
						return Double.parseDouble(conteudo);
					}
					catch (Exception e)
					{
						return null;
					}

				case VALOR_MONETARIO:
					try
					{
						return Double.parseDouble(conteudo);
					}
					catch (Exception e)
					{
						return null;
					}

				case BOOLEANO:
					try
					{
						if (conteudo.equalsIgnoreCase("sim") || conteudo.equalsIgnoreCase("true"))
						{
							return true;
						}
						else
						{
							return false;
						}
					}
					catch (Exception e)
					{
						return null;
					}

				case TEXTO:
					return conteudo;

				case DATA:
					try
					{
						DateTimeFormat numberFormat = DateTimeFormat.getFormat(QITelaRelatorioDinamico.padraoData);
						return numberFormat.parse(conteudo);
					}
					catch (Exception e)
					{
						return null;
					}

				case DATA_HORA:
					try
					{
						DateTimeFormat numberFormat = DateTimeFormat.getFormat(QITelaRelatorioDinamico.padraoDataHora);
						return numberFormat.parse(conteudo);
					}
					catch (Exception e)
					{
						return null;
					}

			}
		}
		return null;
	}

	public void refreshListaFiltros()
	{

	}

	private class ChangeHandlerCampoRelatorio implements ChangeHandler
	{
		@Override
		public void onChange(ChangeEvent event)
		{
			atualizarFiltrosExistentes();
		}
	}
}
