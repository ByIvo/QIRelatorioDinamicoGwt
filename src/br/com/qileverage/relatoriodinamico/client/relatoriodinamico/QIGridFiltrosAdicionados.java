package br.com.qileverage.relatoriodinamico.client.relatoriodinamico;

import java.util.Date;

import br.com.qileverage.relatoriodinamico.client.QITelaRelatorioDinamico;
import br.com.qileverage.relatoriodinamico.entidades.QICampoRelatorio;
import br.com.qileverage.relatoriodinamico.entidades.QIFiltroCampoRelatorio;
import br.com.qileverage.widgets.crud.grid.QIGridColunaOpcaoExcluir;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.Column;

public abstract class QIGridFiltrosAdicionados extends QIGridColunaOpcaoExcluir<QIFiltroCampoRelatorio>
{

	private QICampoRelatorio campoRelatorio;

	public QIGridFiltrosAdicionados()
	{
		super();
	}

	@Override
	public void setarInformacoes()
	{
		super.setarInformacoes();
	}

	@Override
	public void montarTela()
	{
		super.montarTela();

		this.adicionarColuna(new Column<QIFiltroCampoRelatorio, String>(new TextCell())
		{
			@Override
			public String getValue(QIFiltroCampoRelatorio object)
			{
				return object.getCampoRelatorio().getNomeCampo();
			}
		}, "Nome Coluna");

		this.adicionarColuna(new Column<QIFiltroCampoRelatorio, String>(new TextCell())
		{
			@Override
			public String getValue(QIFiltroCampoRelatorio object)
			{
				return object.getTipoFiltro().toString();
			}
		}, "Tipo Filtro");

		this.adicionarColuna(new Column<QIFiltroCampoRelatorio, String>(new TextCell())
		{
			@Override
			public String getValue(QIFiltroCampoRelatorio object)
			{
				return formatarCampoRelatorio(object);
			}
		}, "Valor");
	}

	private String formatarCampoRelatorio(QIFiltroCampoRelatorio filtro)
	{
		if (filtro.getValue() instanceof Date)
		{
			String formato = "dd/MM/yyyyy";
			switch (filtro.getCampoRelatorio().getTipoCampo())
			{
				case DATA:
					formato = QITelaRelatorioDinamico.padraoData;
					break;

				case DATA_HORA:
					formato = QITelaRelatorioDinamico.padraoDataHora;
					break;
			}

			DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(formato);
			return dateTimeFormat.format((Date) filtro.getValue());
		}
		else
		{
			return filtro.getValue().toString();
		}

	}

	@Override
	public void selecionou(QIFiltroCampoRelatorio entidade)
	{

	}

	public QICampoRelatorio getCampoRelatorio()
	{
		return campoRelatorio;
	}

	public void setCampoRelatorio(QICampoRelatorio campoRelatorio)
	{
		this.campoRelatorio = campoRelatorio;
	}

}
