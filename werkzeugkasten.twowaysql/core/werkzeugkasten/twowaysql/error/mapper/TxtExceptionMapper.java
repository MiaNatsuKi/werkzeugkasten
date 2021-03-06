package werkzeugkasten.twowaysql.error.mapper;

import werkzeugkasten.twowaysql.error.handler.EarlyExitHandler;
import werkzeugkasten.twowaysql.nls.Messages;

public class TxtExceptionMapper extends AbstractExceptionMapper {

	public TxtExceptionMapper() {
		add(new EarlyExitHandler(Messages.LABEL_TXT, Messages.REQUIRED_TXT));
	}
}
