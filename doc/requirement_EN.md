# Deployment requirements
	
## Hardware requirements
> The task execution node (data-node) is a multi-thread high concurrency, CPU and memory consumption type application, so it has high requirements on the number of CPU cores and RAM.

- Minimum requirement
	- CPU 4+
	- RAM 4G+

## Software requirements
- kafka Cluster
- zookeeper Cluster
> kafka and data synchronization have used the zk, to achieve distributed, scalable service.
- jdk8+
> Using jdk8 features.

### Resource consumption
> In the case of no message consumption, the memory usage is only about 15M. When messages is flood peak, the CPU and memory will be very high, and the flood peak will recover smoothly after the peak.

