package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.remote.InitialRequest;
import org.junit.runner.RunWith;
import property.generator.InitialRequestGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnitQuickcheck.class)
public class TestInitialRequestProperty {
    @Property
    public void equalsHashcode(@From(InitialRequestGenerator.class) InitialRequest request,
                               @From(InitialRequestGenerator.class) InitialRequest request2) {
        if (request.equals(request2)) {
            assertEquals(request.hashCode(), request2.hashCode());
        }
        if (request.hashCode() != request2.hashCode()) {
            assertNotEquals(request, request2);
        }
    }
}
