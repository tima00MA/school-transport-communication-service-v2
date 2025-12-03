package fs.master.asynccommunicationservice.controller;

import fs.master.asynccommunicationservice.model.MessageDTO;
import fs.master.asynccommunicationservice.model.MessageLogDTO;
import fs.master.asynccommunicationservice.model.Subscription;
import fs.master.asynccommunicationservice.service.AsyncService;
import org.springframework.http.ResponseEntity;import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/async")
public class AsyncController {

    private final AsyncService asyncService;

    public AsyncController(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestBody MessageDTO messageDTO) {
        asyncService.publishMessage(messageDTO);
        return ResponseEntity.ok("Message published to queue: " + messageDTO.queue());
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody Subscription subscription) {
        asyncService.subscribe(subscription);
        return ResponseEntity.ok("Subscribed to queue: " + subscription.getQueue());
    }

    @GetMapping("/logs")
    public ResponseEntity<List<MessageLogDTO>> getLogs() {
        List<MessageLogDTO> logs = asyncService.getMessageLogs();
        return ResponseEntity.ok(logs);
    }
}