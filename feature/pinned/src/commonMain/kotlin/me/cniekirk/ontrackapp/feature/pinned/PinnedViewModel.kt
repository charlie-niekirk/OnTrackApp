package me.cniekirk.ontrackapp.feature.pinned

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.launch
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.core.domain.repository.PinnedServicesRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@Inject
@ViewModelKey(PinnedViewModel::class)
@ContributesIntoMap(scope = AppScope::class, binding = binding<ViewModel>())
class PinnedViewModel(
    private val pinnedServicesRepository: PinnedServicesRepository
) : ViewModel(), ContainerHost<PinnedState, PinnedEffect> {

    override val container = container<PinnedState, PinnedEffect>(PinnedState()) {
        fetchPinnedServices()
    }

    fun serviceSelected(serviceDetailRequest: ServiceDetailRequest) = intent {
        postSideEffect(PinnedEffect.NavigateToServiceDetails(serviceDetailRequest))
    }

    private fun fetchPinnedServices() {
        viewModelScope.launch {
            pinnedServicesRepository.pinnedServices.collect { pinnedServices ->
                intent {
                    reduce {
                        state.copy(
                            isLoading = false,
                            pinnedServices = pinnedServices
                        )
                    }
                }
            }
        }
    }
}
