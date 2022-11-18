package uk.gorodny;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import uk.gorodny.wrapper.Wrapper;

public class Main {
    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(1000)
                .build();

        client.subscribe("external-demo")
                .lockDuration(1000)
                .handler(new ExternalTaskHandler() {
                    @Override
                    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
                        String sum = Wrapper.getString(externalTask.getVariable("sum"));
                        String rate = Wrapper.getString(externalTask.getVariable("rate"));
                        String monthlyPayment = Wrapper.getString(externalTask.getVariable("monthlyPayment"));

                        System.out.printf("Sum: %s, Rate: %s, Monthly payment: %s%n", sum, rate, monthlyPayment);

                        externalTaskService.complete(externalTask);
                    }
                })
                .open();
    }
}
