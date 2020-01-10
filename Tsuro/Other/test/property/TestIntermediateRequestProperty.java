package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.remote.IntermediateRequest;
import org.junit.runner.RunWith;
import property.generator.IntermediateRequestGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnitQuickcheck.class)
public class TestIntermediateRequestProperty {
    @Property
    public void equalsHashcode(@From(IntermediateRequestGenerator.class) IntermediateRequest request,
                               @From(IntermediateRequestGenerator.class) IntermediateRequest request2) {
        if (request.equals(request2)) {
            assertEquals(request.hashCode(), request2.hashCode());
        }
        if (request.hashCode() != request2.hashCode()) {
            assertNotEquals(request, request2);
        }
    }
}