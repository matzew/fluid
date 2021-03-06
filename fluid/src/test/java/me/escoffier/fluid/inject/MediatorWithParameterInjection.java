package me.escoffier.fluid.inject;

import io.reactivex.Flowable;
import me.escoffier.fluid.annotations.Inbound;
import me.escoffier.fluid.annotations.Outbound;
import me.escoffier.fluid.annotations.Transformation;
import me.escoffier.fluid.models.Message;
import me.escoffier.fluid.models.Sink;
import me.escoffier.fluid.models.Source;
import org.reactivestreams.Publisher;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MediatorWithParameterInjection {


  @Transformation
  public void transform(
    @Inbound("my-source") Source<String> s1,
    @Inbound("my-source") Publisher<Message<String>> s2,
    @Inbound("my-source") Publisher<String> s3,
    @Inbound("my-source") Flowable<Message<String>> s4,
    @Inbound("my-source") Flowable<String> s5,
    @Outbound("my-sink") Sink<String> sink
  ) {

    s1.mapPayload(String::toUpperCase).mapPayload(s -> "S1-" + s).to(sink);

    Flowable.fromPublisher(s2).map(Message::payload)
      .map(s -> "S2-" + s.toUpperCase()).flatMapCompletable(sink::dispatch)
      .subscribe();

    Flowable.fromPublisher(s3)
      .map(s -> "S3-" + s.toUpperCase()).flatMapCompletable(sink::dispatch)
      .subscribe();

    s4.map(Message::payload)
      .map(s -> "S4-" + s.toUpperCase()).flatMapCompletable(sink::dispatch)
      .subscribe();

    s5.map(s -> "S5-" + s.toUpperCase()).flatMapCompletable(sink::dispatch)
      .subscribe();
  }
}
