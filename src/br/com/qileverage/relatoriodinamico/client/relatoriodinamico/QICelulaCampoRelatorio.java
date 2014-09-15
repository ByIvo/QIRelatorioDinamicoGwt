package br.com.qileverage.relatoriodinamico.client.relatoriodinamico;

import br.com.qileverage.relatoriodinamico.entidades.QICampoRelatorio;
import br.com.qileverage.widgets.cellwidgets.QICelulaTexto;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;

public class QICelulaCampoRelatorio extends QICelulaTexto<QICampoRelatorio>
{

	@Override
	public Label getTexto(QICampoRelatorio entidade)
	{
		Label retorno = new Label(entidade.getNomeCampo());

		retorno.getElement().getStyle().setTextIndent(10, Unit.PX);

		return retorno;
	}

}
