package controllers.actions;

import javax.persistence.PersistenceException;
import javax.validation.ValidationException;

import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Result;

public class CatchPersistenceAndValidationExceptionAction extends Action.Simple {
	@Override
	public Promise<Result> call(Context ctx) throws Throwable {
		try{
			return delegate.call(ctx);
		}catch(PersistenceException | ValidationException e){
			return Promise.promise(() -> status(Http.Status.CONFLICT));
		}
	}

}
