package me.escoffier.fluid.example;

import hu.akarnokd.rxjava2.math.MathFlowable;
import io.vertx.core.json.JsonObject;
import me.escoffier.fluid.annottions.Port;
import me.escoffier.fluid.annottions.Transformation;
import me.escoffier.fluid.constructs.Sink;
import me.escoffier.fluid.constructs.Source;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Mediator {

    @Port("sensor")
    Source<JsonObject> input;

    @Port("eb-average")
    Sink<Double> output;


    @Transformation
    public void mediation() {
        input
            .transform(json -> json.getDouble("data"))
            .transformFlow(flow ->
                flow.window(5)
                    .flatMap(MathFlowable::averageDouble)
            )

            .to(output);
    }

}